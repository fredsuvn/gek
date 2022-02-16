@file:JvmName("BNet")

package xyz.srclab.common.net

import java.nio.channels.ClosedSelectorException
import java.nio.channels.SelectionKey
import java.nio.channels.Selector

fun nioListen(selector: Selector, handler: (SelectionKey) -> Unit) {
    while (true) {
        try {
            val readyChannels = selector.select()
            if (readyChannels == 0) {
                continue
            }
            val selectedKeys = selector.selectedKeys()
            val iterator = selectedKeys.iterator()
            while (iterator.hasNext()) {
                val key = iterator.next()
                handler(key)
                iterator.remove();
            }
        } catch (e: ClosedSelectorException) {
            break
        }
    }
}