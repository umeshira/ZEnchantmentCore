package io.zivoric.enchantmentcore.autoenchlistener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import io.zivoric.enchantmentcore.EnchantmentCore;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * {@link AutoEnchListener} with packet events
 */
public class ProtocolAutoEnchListener extends AutoEnchListener {
    public ProtocolAutoEnchListener(EnchantmentCore core) {
        super(core);
    }

    @Override
    public void setup() {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(getCore(), PacketType.Play.Server.SET_SLOT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                ItemStack item = packet.getItemModifier().read(0);
                ItemStack clone = item.clone();
                addLoreLoop(clone);
                packet.getItemModifier().write(0, clone);
            }
        });

        manager.addPacketListener(new PacketAdapter(getCore(), PacketType.Play.Server.WINDOW_ITEMS) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                List<ItemStack> items = packet.getItemListModifier().readSafely(0);
                items.replaceAll(ItemStack::clone);
                addLoreLoop(items);
                packet.getItemListModifier().write(0, items);
            }
        });
    }

    @Override
    public void unregister() {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.removePacketListeners(getCore());
    }
}
