package org.jellyfin.mobile.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.Inet6Address
import java.net.InetAddress
import java.net.UnknownHostException
import timber.log.Timber

/**
 * DNS-based IP4P address resolver.
 *
 * Resolves a hostname via DNS and checks whether any of its IPv6 (AAAA) addresses
 * conform to the IP4P format. If found, the address is decoded into an IPv4:port pair.
 *
 * This enables the standard IP4P workflow: natmap updates a DNS AAAA record with the
 * IP4P-encoded address, and the client resolves the domain to discover the real endpoint.
 */
object Ip4pResolver {

    /**
     * Resolves [hostname] via DNS and checks AAAA results for IP4P-encoded addresses.
     *
     * @return Decoded [Ip4pParser.Ip4pData] if an IP4P address is found in the DNS results,
     *         or `null` if the hostname cannot be resolved or has no IP4P AAAA records.
     */
    suspend fun resolve(hostname: String): Ip4pParser.Ip4pData? = withContext(Dispatchers.IO) {
        // Skip DNS resolution if the input already looks like an IP4P address
        if (Ip4pParser.isIp4p(hostname)) {
            return@withContext Ip4pParser.parse(hostname)
        }

        try {
            val addresses = InetAddress.getAllByName(hostname)
            Timber.d("Resolved $hostname → ${addresses.map { it.hostAddress }}")

            addresses
                .filterIsInstance<Inet6Address>()
                .map { it.hostAddress?.lowercase() }
                .firstNotNullOfOrNull { addr ->
                    if (addr != null) {
                        val data = Ip4pParser.parse(addr)
                        if (data != null) {
                            Timber.i("Found IP4P AAAA record for $hostname: $addr → ${data.ipv4}:${data.port}")
                        }
                        data
                    } else null
                }
        } catch (e: UnknownHostException) {
            Timber.d(e, "DNS resolution failed for $hostname")
            null
        }
    }

    /**
     * Resolves an IP4P hostname (raw IP4P address or domain with IP4P AAAA record)
     * to a connectable HTTP URL.
     *
     * Tries raw [Ip4pParser] parsing first (instant), then falls back to DNS AAAA
     * resolution. Returns `null` if the hostname cannot be resolved as IP4P.
     */
    suspend fun resolveToUrl(hostname: String): String? {
        // Try raw IP4P address first (instant, no network)
        Ip4pParser.toUrl(hostname)?.let { url ->
            Timber.d("IP4P raw parse: $hostname → $url")
            return url
        }
        // Try DNS AAAA resolution
        val data = resolve(hostname)
        if (data != null) {
            val url = "http://${data.ipv4}:${data.port}"
            Timber.i("IP4P DNS resolved: $hostname → $url")
            return url
        }
        return null
    }
}
