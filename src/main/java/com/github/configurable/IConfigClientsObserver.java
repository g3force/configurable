/*
 * *********************************************************
 * Copyright (c) 2009 - 2015, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: Oct 27, 2015
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * *********************************************************
 */
package com.github.configurable;

/**
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 */
public interface IConfigClientsObserver
{
	/**
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 * @param cc
	 */
	void onNewConfigClient(IConfigClient cc);
}
