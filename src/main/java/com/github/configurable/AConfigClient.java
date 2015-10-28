/*
 * *********************************************************
 * Copyright (c) 2009 - 2011, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 28.11.2011
 * Author(s): Gero
 * *********************************************************
 */
package com.github.configurable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.NodeCombiner;
import org.apache.commons.configuration.tree.UnionCombiner;
import org.apache.log4j.Logger;


/**
 * Base implementation for {@link IConfigClient}
 * 
 * @author Gero
 */
public abstract class AConfigClient implements IConfigClient
{
	@SuppressWarnings("unused")
	private static final Logger			log				= Logger.getLogger(AConfigClient.class.getName());
																		
	private static final String			XML_ENCODING	= "UTF-8";
																		
	private final String						name;
	private final String						path;
	private final List<IConfigObserver>	observers		= new CopyOnWriteArrayList<IConfigObserver>();
																		
	private HierarchicalConfiguration	config			= new HierarchicalConfiguration();
																		
																		
	/**
	 * @param name
	 * @param path
	 */
	public AConfigClient(final String name, final String path)
	{
		super();
		this.name = name;
		this.path = path;
	}
	
	
	@Override
	public HierarchicalConfiguration getFileConfig()
	{
		String fileName = name + ".xml";
		String filePath = Paths.get(path, fileName).toString();
		XMLConfiguration cfg = new XMLConfiguration();
		try
		{
			cfg.setDelimiterParsingDisabled(true);
			cfg.setFileName(fileName);
			cfg.load(filePath);
		} catch (final ConfigurationException err)
		{
			log.error("Unable to load config '" + name + "' from '" + filePath + "':", err);
		}
		
		return cfg;
	}
	
	
	/**
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	@Override
	public HierarchicalConfiguration getCombinedConfig()
	{
		// Create and initialize the node combiner
		NodeCombiner combiner = new UnionCombiner();
		combiner.addListNode("table"); // mark table as list node
		// this is needed only if there are ambiguities
		
		// Construct the combined configuration
		CombinedConfiguration cc = new CombinedConfiguration(combiner);
		cc.addConfiguration(getLocalConfig());
		cc.addConfiguration(getFileConfig());
		
		return cc;
	}
	
	
	@Override
	public void loadCombinedConfig()
	{
		config = getCombinedConfig();
		notifyLoadConfig();
	}
	
	
	/**
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	@Override
	public void loadFileConfig()
	{
		config = getFileConfig();
		notifyLoadConfig();
	}
	
	
	/**
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	@Override
	public void loadLocalConfig()
	{
		config = getLocalConfig();
		notifyLoadConfig();
	}
	
	
	/**
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public void notifyLoadConfig()
	{
		for (IConfigObserver o : observers)
		{
			o.onLoad(config);
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
			XMLConfiguration xmlConfig = new XMLConfiguration(config);
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
	public void addObserver(final IConfigObserver observer)
	{
		observers.add(observer);
	}
	
	
	@Override
	public void removeObserver(final IConfigObserver observer)
	{
		observers.remove(observer);
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
	public final HierarchicalConfiguration getCurrentConfig()
	{
		return config;
	}
}
