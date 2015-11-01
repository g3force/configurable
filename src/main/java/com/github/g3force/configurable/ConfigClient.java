/*
 * *********************************************************
 * Copyright (c) 2009 - 2011, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 28.11.2011
 * Author(s): Gero
 * *********************************************************
 */
package com.github.g3force.configurable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;


/**
 * Base implementation for {@link IConfigClient}
 * 
 * @author Gero
 */
public class ConfigClient implements IConfigClient
{
	@SuppressWarnings("unused")
	private static final Logger					log				= Logger.getLogger(ConfigClient.class.getName());
																				
	private static final String					XML_ENCODING	= "UTF-8";
																				
	private final String								name;
	private final String								path;
	private final List<IConfigObserver>			observers		= new CopyOnWriteArrayList<IConfigObserver>();
																				
	private final ConfigAnnotationProcessor	cap;
	private final Set<Class<?>>					classes			= new LinkedHashSet<Class<?>>();
																				
	private HierarchicalConfiguration			config			= new HierarchicalConfiguration();
																				
																				
	/**
	 * @param path
	 * @param name
	 */
	public ConfigClient(final String path, final String name)
	{
		this.name = name;
		this.path = path;
		cap = new ConfigAnnotationProcessor(name);
	}
	
	
	@Override
	public void addObserver(final IConfigObserver observer)
	{
		observers.add(observer);
	}
	
	
	@Override
	public void removeObserver(final IConfigObserver observer)
	{
		observers.remove(observer);
	}
	
	
	/**
	 * Add a configurable class
	 * 
	 * @param clazz
	 */
	public void putClass(final Class<?> clazz)
	{
		classes.add(clazz);
		cap.loadClass(clazz, false);
	}
	
	
	/**
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public void applyConfig()
	{
		cap.loadConfiguration(config);
		cap.applyAll();
		notifyAppliedConfig();
	}
	
	
	@Override
	public HierarchicalConfiguration getFileConfig()
	{
		String fileName = name + ".xml";
		Path fPath = Paths.get(path, fileName);
		String filePath = fPath.toString();
		XMLConfiguration cfg = new XMLConfiguration();
		try
		{
			cfg.setDelimiterParsingDisabled(true);
			cfg.setFileName(fileName);
			if (Files.exists(fPath))
			{
				cfg.load(filePath);
			}
		} catch (final ConfigurationException err)
		{
			log.error("Unable to load config '" + name + "' from '" + filePath + "':", err);
		}
		
		return cfg;
	}
	
	
	/**
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public void notifyAppliedConfig()
	{
		for (IConfigObserver o : observers)
		{
			o.afterApply(this);
		}
	}
	
	
	/**
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 * @return
	 */
	@Override
	public boolean saveCurrentConfig()
	{
		String fileName = name + ".xml";
		String filePath = Paths.get(path, fileName).toString();
		
		FileOutputStream targetFile = null;
		OutputStream prettyOut = null;
		try
		{
			targetFile = new FileOutputStream(filePath, false);
			
			prettyOut = new PrettyXMLOutputStream(targetFile, XML_ENCODING);
			XMLConfiguration xmlConfig = new XMLConfiguration(cap.getMinimalConfig());
			xmlConfig.save(prettyOut, XML_ENCODING);
			
		} catch (final ConfigurationException err)
		{
			log.error("Unable to save config '" + name + "' to '" + filePath + "'.");
			return false;
		} catch (final FileNotFoundException err)
		{
			log.error("Unable to access the file to save the config to: " + filePath, err);
		} finally
		{
			try
			{
				if (prettyOut != null)
				{
					prettyOut.close();
				}
				if (targetFile != null)
				{
					targetFile.close();
				}
			} catch (IOException err)
			{
				log.error("Error while saving config: Unable to close streams!", err);
			}
		}
		
		return true;
	}
	
	
	@Override
	public final String getName()
	{
		return name;
	}
	
	
	@Override
	public final String getPath()
	{
		return path;
	}
	
	
	@Override
	public final HierarchicalConfiguration loadConfig()
	{
		cap.loadConfiguration(getFileConfig());
		classes.forEach(clazz -> cap.loadClass(clazz, false));
		config = cap.getEffectiveConfig();
		return config;
	}
	
	
	@Override
	public final HierarchicalConfiguration getConfig()
	{
		return config;
	}
	
	
	/**
	 * @return the cap
	 */
	final ConfigAnnotationProcessor getCap()
	{
		return cap;
	}
}
