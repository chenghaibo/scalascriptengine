package com.googlecode.scalascriptengine

import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.xml.sax.helpers.NewInstance

/**
 * @author kostantinos.kougios
 *
 * 5 Jan 2012
 */
@RunWith(classOf[JUnitRunner])
class ConstructorsSuite extends FunSuite with ShouldMatchers {

	val constructors = new Constructors(classOf[ConstructorsSuiteTest])

	test("no arg constructor") {
		val t = constructors.newInstance
		t.s should be === "noarg"
	}

	test("one arg constructor") {
		val t = constructors.newInstance("one-arg")
		t.s should be === "one-arg"
	}

	test("two arg constructor") {
		val t = constructors.newInstance("two-arg", 2)
		t.s should be === "two-arg"
		t.x should be === 2
	}

	test("three arg constructor") {
		val t = constructors.newInstance("three-arg", 3, 5.toDouble)
		t.s should be === "three-arg"
		t.x should be === 3
		t.y should be === 5.toDouble
	}

	test("four arg constructor") {
		val t = constructors.newInstance("four-arg", 3, 5.toDouble, true)
		t.s should be === "four-arg"
		t.x should be === 3
		t.y should be === 5.toDouble
		t.z should be(true)
	}
}

class ConstructorsSuiteTest(val s: String, val x: Int, val y: Double, val z: Boolean) {
	def this() = this("noarg", 0, 0, false)
	def this(s: String) = this(s, 1, 0, false)
	def this(s: String, x: Int) = this(s, x, 0, false)
	def this(s: String, x: Int, y: Double) = this(s, x, y, false)
}