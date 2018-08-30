package com.github.sghill.gradle

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Logger {
    private val logger: Logger by lazy {
        LoggerFactory.getLogger(DistributionShaPlugin::class.java)
    }
    private const val marker: String = "[dist-sha]"

    fun info(msg: String) {
        logger.info("$marker $msg")
    }

    fun warn(msg: String) {
        logger.warn("$marker $msg")
    }

    fun debug(msg: String) {
        logger.debug("$marker $msg")
    }
}
