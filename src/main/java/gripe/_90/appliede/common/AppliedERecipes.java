package gripe._90.appliede.common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import ae2.core.definitions.AEBlocks;
import ae2.core.definitions.AEItems;
import ae2.core.definitions.AEParts;
import gripe._90.appliede.AppliedE;
import moze_intel.projecte.PECore;
import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.item.ItemStack;

public final class AppliedERecipes {
    private static final String CUSTOM_CONVERSIONS = """
            {
              "comment": "Default AppliedE values and conversions for ProjectE 1.12.",
              "groups": {
                "condenser": {
                  "conversions": [
                    {
                      "output": "ae2:singularity|0",
                      "ingredients": {
                        "ae2:cell_component_64k|0": 1
                      }
                    }
                  ]
                }
              },
              "values": {
                "before": {
                  "ae2:certus_quartz_crystal|0": 256,
                  "ae2:sky_stone_block|0": 256,
                  "ae2:calculation_processor_press|0": 2048,
                  "ae2:engineering_processor_press|0": 2048,
                  "ae2:logic_processor_press|0": 2048,
                  "ae2:silicon_press|0": 2048,
                  "ae2:cable_anchor|0": 32,
                  "ae2:matter_ball|0": 512,
                  "ae2:quantum_entangled_singularity|0": "free"
                }
              }
            }
            """;
    private static boolean hooksRegistered;

    private AppliedERecipes() {
    }

    public static void initRuntimeHooks() {
        if (hooksRegistered) {
            return;
        }

        registerDefaultEmcValues();
        registerDefaultConversions();
        installDefaultCustomConversions();
        hooksRegistered = true;
    }

    private static void registerDefaultEmcValues() {
        registerEmc(AEItems.CERTUS_QUARTZ_CRYSTAL.stack(), 256);
        registerEmc(AEBlocks.SKY_STONE_BLOCK.stack(), 256);
        registerEmc(AEItems.CALCULATION_PROCESSOR_PRESS.stack(), 2048);
        registerEmc(AEItems.ENGINEERING_PROCESSOR_PRESS.stack(), 2048);
        registerEmc(AEItems.LOGIC_PROCESSOR_PRESS.stack(), 2048);
        registerEmc(AEItems.SILICON_PRESS.stack(), 2048);
        registerEmc(AEParts.CABLE_ANCHOR.stack(), 32);
        registerEmc(AEItems.MATTER_BALL.stack(), 512);
    }

    private static void registerEmc(ItemStack stack, long emc) {
        ProjectEAPI.getEMCProxy().registerCustomEMC(stack, emc);
    }

    private static void registerDefaultConversions() {
        Map<Object, Integer> ingredients = new LinkedHashMap<>();
        ingredients.put(AEItems.CELL_COMPONENT_64K.stack(), 1);

        ProjectEAPI.getConversionProxy().addConversion(1, AEItems.SINGULARITY.stack(), ingredients);
    }

    private static void installDefaultCustomConversions() {
        if (PECore.CONFIG_DIR == null) {
            AppliedE.LOGGER.warn("ProjectE config directory is not initialized; skipping AppliedE custom conversions file.");
            return;
        }

        Path conversions = PECore.CONFIG_DIR.toPath().resolve("customConversions");
        Path appliedE = conversions.resolve("appliede.json");

        try {
            Files.createDirectories(conversions);

            if (Files.notExists(appliedE)) {
                Files.writeString(appliedE, CUSTOM_CONVERSIONS, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            AppliedE.LOGGER.warn("Could not install AppliedE ProjectE custom conversions file.", e);
        }
    }
}
