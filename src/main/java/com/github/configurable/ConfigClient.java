/*
 * *********************************************************
 * Copyright (c) 2009 - 2014, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: Aug 25, 2014
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * *********************************************************
 */
package com.github.configurable;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.configuration.HierarchicalConfiguration;


/**
 * Generic config client for Configurables
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 */
public class ConfigClient extends AConfigClient
{
	private final ConfigAnnotationProcessor	cap	= new ConfigAnnotationProcessor();
	private final Set<Class<?>>					classes;
															
															
	/**
	 * @param path
	 * @param name
	 */
	public ConfigClient(final String path, final String name)
	{
		this(path, name, new LinkedHashSet<Class<?>>());
	}
	
	
	/**
	 * Construct with initial classes
	 * 
	 * @param path
	 * @param name
	 * @param classes
	 */
	public ConfigClient(final String path, final String name, final Set<Class<?>> classes)
	{
		super(name, path);
		this.classes = classes;
	}
	
	
	@Override
	public HierarchicalConfiguration getLocalConfig()
	{
		return cap.getDefaultConfig(classes, getName());
	}
	
	
	/**
	 * Apply all config values with given spezi. If spezi=="", apply default values.
	 * 
	 * @param obj The instance where all fields should be set.
	 * @param spezi
	 */
	public void applyConfigToObject(final Object obj, final String spezi)
	{
		cap.apply(obj, spezi);
	}
	
	
	/**
	 * Apply spezi
	 * 
	 * @param spezi
	 */
	public void applySpezi(final String spezi)
	{
		cap.apply(spezi);
	}
	
	
	/**
	 * Add a configurable class
	 * 
	 * @param clazz
	 */
	public void putClass(final Class<?> clazz)
	{
		classes.add(clazz);
	}
	
	
	/**
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public void applyConfig()
	{
		cap.loadConfiguration(getCurrentConfig());
		cap.applyAll();
	}
}
