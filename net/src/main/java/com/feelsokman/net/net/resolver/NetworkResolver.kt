package com.feelsokman.net.net.resolver

/**
 * Interface for hiding network connectivity resolution
 */
interface NetworkResolver {

    fun isConnected(): Boolean
}