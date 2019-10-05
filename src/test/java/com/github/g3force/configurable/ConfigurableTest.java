/*
 * *********************************************************
 * Copyright (c) 2009 - 2016, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: Mar 10, 2016
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * *********************************************************
 */
package com.github.g3force.configurable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.g3force.configurable.ConfigClass1.ETest;


public class ConfigurableTest
{
	private static final Logger log = LogManager.getLogger(ConfigurableTest.class.getName());

	private static final String CATEGORY = "default";
	private final Path configFilePath = Paths.get("config", CATEGORY + ".xml");


	@Before
	public void before()
	{
		deleteConfigFile();
	}


	private void deleteConfigFile()
	{
		try
		{
			Path p = Paths.get("config", CATEGORY + ".xml");
			if (p.toFile().exists())
			{
				Files.delete(p);
			}
		} catch (IOException e)
		{
			log.error("", e);
		}
	}


	@Test
	public void testSave()
	{
		// save current config
		ConfigRegistration.save(CATEGORY);
		// the config file will only contain values that differ from their default value
		Assert.assertTrue(configFilePath.toFile().exists());
	}


	@Test
	public void testChangeValue()
	{
		Assert.assertFalse(ConfigClass1.testBool);

		// change a value
		ConfigClass1.testBool = true;
		// reread the value from all classes into internal config
		ConfigRegistration.readClasses(CATEGORY);

		// change value back
		ConfigClass1.testBool = false;
		ConfigClass1.testEnum = ConfigClass1.ETest.TWO;

		// apply internal config
		ConfigRegistration.applyConfig(CATEGORY);
		// value should be changed back to true
		Assert.assertTrue(ConfigClass1.testBool);
		Assert.assertSame(ConfigClass1.testEnum, ETest.ONE);

		// save internal config to file
		ConfigRegistration.save(CATEGORY);

		// config file should exist and contain testbool
		Assert.assertTrue(configFilePath.toFile().exists());
	}


	@Test
	public void testSpezi()
	{
		// default value for fields with spezi is the empty spezi
		Assert.assertEquals(1, ConfigClass2.testSpezi, 0.0001);

		ConfigRegistration.applySpezi(CATEGORY, "CONF1");
		Assert.assertEquals(2, ConfigClass2.testSpezi, 0.0);
		ConfigRegistration.applySpezi(CATEGORY, "CONF2");
		Assert.assertEquals(3, ConfigClass2.testSpezi, 0.0);
		ConfigRegistration.applySpezi(CATEGORY, "");
		Assert.assertEquals(1, ConfigClass2.testSpezi, 0.0);
	}


	@Test
	public void testInstance()
	{
		ConfigClass3 cc = new ConfigClass3();
		Assert.assertEquals(2, cc.testSpezi, 0.0001);
	}


	@Test
	public void testCallback()
	{
		ConfigRegistration.registerConfigurableCallback(CATEGORY, new IConfigObserver()
		{
			@Override
			public void afterApply(final IConfigClient configClient)
			{
				ConfigClass1.testDouble = 10;
			}
		});

		ConfigClass1.testDouble = -1;

		ConfigRegistration.applyConfig(CATEGORY);

		Assert.assertEquals(10, ConfigClass1.testDouble, 0.0001);
	}


	@Test
	public void testOverride()
	{
		ConfigClass1.testDouble = 1;
		ConfigRegistration.overrideConfig(ConfigClass1.class, CATEGORY, "testDouble", "42");
		Assert.assertEquals(42.0, ConfigClass1.testDouble, 1e-10);
	}


	@Test
	public void testDefValue()
	{
		ConfigAnnotationProcessor cap = new ConfigAnnotationProcessor("read");
		cap.loadClass(ConfigClass4.class, false);
		assertThat(ConfigClass4.testBoolFalse).isEqualTo(false);
		assertThat(ConfigClass4.testBoolTrue).isEqualTo(true);
		assertThat(ConfigClass4.testEnum.name()).isEqualTo("ONE");
		assertThat(ConfigClass4.testEnumDefValue.name()).isEqualTo("TWO");
		assertThat(ConfigClass4.testDouble).isEqualTo(1.0, within(1e-10));
		assertThat(ConfigClass4.testDoubleWithDefault).isEqualTo(2.0, within(1e-10));
		assertThat(ConfigClass4.testDefaultDifferent).isEqualTo(6.0);
		assertThat(ConfigClass4.testStoredDifferent).isEqualTo(42.0, within(1e-10));
	}
}
