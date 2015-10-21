/*
 * *********************************************************
 * Copyright (c) 2009 - 2011, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 25.11.2011
 * Author(s): Gero
 * *********************************************************
 */
package com.github.configurable;

import org.apache.commons.configuration.HierarchicalConfiguration;


/**
 * @author Gero
 */
public interface IConfigClient extends IConfigObserver
{
	/**
	 * @return The common name of this {@link IConfigClient}
	 */
	String getName();
	
	
	/**
	 * @return The path to the config-files
	 */
	String getConfigPath();
	
	
	/**
	 * @return The unique key which is used to store the user-settings of Sumatra
	 */
	String getConfigKey();
	
	
	/**
	 * @return the defaultValue
	 */
	String getDefaultValue();
	
	
	/**
	 * @return Whether this config should be editable
	 */
	boolean isEditable();
	
	
	/**
	 * Get default configuration that should be merged with existing config.
	 * 
	 * @return
	 */
	HierarchicalConfiguration getDefaultConfig();
	
	
	/**
	 * 
	 */
	void clearObservers();
	
	
	/**
	 * Is the config file required? Must it exist?
	 * 
	 * @return
	 */
	boolean isRequired();
}
