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
 * Used to observe a config client
 * 
 * @author Gero
 */
public interface IConfigObserver
{
	/**
	 * @param newConfig
	 */
	void onLoad(HierarchicalConfiguration newConfig);
}
