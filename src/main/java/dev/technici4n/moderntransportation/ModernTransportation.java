package dev.technici4n.moderntransportation;

import dev.technici4n.moderntransportation.network.energy.EnergyCache;
import dev.technici4n.moderntransportation.network.NetworkManager;
import dev.technici4n.moderntransportation.init.MtBlocks;
import dev.technici4n.moderntransportation.init.MtItems;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

@Mod("moderntransportation")
public class ModernTransportation {
    public static final Logger LOGGER = LogManager.getLogger("Modern Transportation");

    public ModernTransportation() {
        NetworkManager.registerCacheClass(EnergyCache.class, EnergyCache::new);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addGenericListener(Block.class, (Consumer<RegistryEvent.Register<Block>>) event -> MtBlocks.init());
        modEventBus.addGenericListener(Item.class, (Consumer<RegistryEvent.Register<Item>>) event -> MtItems.init());
        modEventBus.addGenericListener(BlockEntityType.class, (Consumer<RegistryEvent.Register<BlockEntityType<?>>>) event -> MtBlocks.Bet.init());

        MinecraftForge.EVENT_BUS.addListener((Consumer<FMLServerStoppedEvent>) event -> NetworkManager.onServerStopped());
        MinecraftForge.EVENT_BUS.addListener((Consumer<TickEvent.ServerTickEvent>) event -> {
            if (event.phase == TickEvent.Phase.END) {
                NetworkManager.onEndTick();
            }
        });

        if (Dist.CLIENT.isClient()) {
            ModernTransportationClient.initClient();
        }

        LOGGER.info("Successfully constructed Modern Transportation!");
    }
}
