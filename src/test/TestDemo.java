package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import jp.ac.kobe.stu.watanabe.hogehoge;

public class TestDemo {

	static hogehoge hoge;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		hoge = new hogehoge();
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		int a = 10;
		int b = 10;
		int expected = 20;
		assertTrue(expected == hoge.add(a, b));

		
	}

	
}
