package com.googlecode.scalascriptengine
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.io.File

import scalascriptengine._

/**
 * @author kostantinos.kougios
 *
 * 23 Dec 2011
 */
@RunWith(classOf[JUnitRunner])
class ScalaClassLoaderSuite extends FunSuite with ShouldMatchers {
	val sourceDir = new File("testfiles/ScalaClassLoaderSuite")
	val classPath = new File("testfiles/lib").listFiles.filter(_.getName.endsWith(".jar")).toSet + new File("target/test-classes")

	test("will load a class") {
		val destDir = newTmpDir("dynamicclass")
		cleanDestinationAndCopyFromSource(new File(sourceDir, "v1/test"), destDir)
		val scl = new ScalaClassLoader(destDir, classPath)
		scl.loadAll
		val tct: TestClassTrait = scl.newInstance("test.Test")
		tct.result should be === "v1"
	}

	test("will re-load a class") {
		val destDir = newTmpDir("dynamicclass")
		cleanDestinationAndCopyFromSource(new File(sourceDir, "v1/test"), destDir)
		val scl = new ScalaClassLoader(destDir, classPath)
		scl.loadAll
		val tctV1: TestClassTrait = scl.newInstance("test.Test")
		tctV1.result should be === "v1"

		cleanDestinationAndCopyFromSource(new File(sourceDir, "v2/test"), destDir)
		scl.loadAll
		val tctV2: TestClassTrait = scl.newInstance("test.Test")
		tctV2.result should be === "v2"
	}

	test("reloading a class propagates to dependent classes") {
		val destDir = newTmpDir("dynamicclass")
		cleanDestinationAndCopyFromSource(new File(sourceDir, "v1/test"), destDir)
		val scl = new ScalaClassLoader(destDir, classPath)
		scl.loadAll
		val tctV1: TestClassTrait = scl.newInstance("test.TestDep")
		tctV1.result should be === "TestDep:v1"

		cleanDestinationAndCopyFromSource(new File(sourceDir, "v2/test"), destDir)
		scl.loadAll
		val tctV2: TestClassTrait = scl.newInstance("test.TestDep")
		tctV2.result should be === "TestDep:v2"
	}

	test("using both v1 and v2 classes") {
		val destDir = newTmpDir("dynamicclass")
		cleanDestinationAndCopyFromSource(new File(sourceDir, "v1/test"), destDir)
		val scl = new ScalaClassLoader(destDir, classPath)
		scl.loadAll

		val tctV1: TestClassTrait = scl.newInstance("test.Test")
		val tcpV1: TestParamTrait = scl.newInstance("test.TestParam")
		tcpV1.result(tctV1) should be === "TP:v1"

		cleanDestinationAndCopyFromSource(new File(sourceDir, "v2/test"), destDir)
		scl.loadAll

		val tcpV2: TestParamTrait = scl.newInstance("test.TestParam")
		tcpV2.result(tctV1) should be === "TP:v1"

		val tctV2: TestClassTrait = scl.newInstance("test.Test")
		tcpV2.result(tctV2) should be === "TP:v2"
	}
}