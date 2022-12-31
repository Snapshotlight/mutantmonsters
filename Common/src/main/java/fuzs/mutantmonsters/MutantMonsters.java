package fuzs.mutantmonsters;

import fuzs.mutantmonsters.entity.CreeperMinionEntity;
import fuzs.mutantmonsters.entity.EndersoulCloneEntity;
import fuzs.mutantmonsters.entity.mutant.*;
import fuzs.mutantmonsters.init.ModRegistry;
import fuzs.mutantmonsters.network.S2CAnimationMessage;
import fuzs.mutantmonsters.network.S2CMutantEndermanHeldBlockMessage;
import fuzs.mutantmonsters.network.S2CMutantLevelParticlesMessage;
import fuzs.mutantmonsters.network.S2CSeismicWaveFluidParticlesMessage;
import fuzs.mutantmonsters.network.client.C2SCreeperMinionTrackerMessage;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.core.ModConstructor;
import fuzs.puzzleslib.core.ModLoaderEnvironment;
import fuzs.puzzleslib.init.PotionBrewingRegistry;
import fuzs.puzzleslib.network.MessageDirection;
import fuzs.puzzleslib.network.NetworkHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.levelgen.Heightmap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MutantMonsters implements ModConstructor {
    public static final String MOD_ID = "mutantbeasts";
    public static final String MOD_NAME = "Mutant Monsters";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final NetworkHandler NETWORK = CommonFactories.INSTANCE.network(MOD_ID);

    @Override
    public void onConstructMod() {
        ModRegistry.touch();
        registerMessages();
    }

    @Override
    public void onCommonSetup() {
        PotionBrewingRegistry.INSTANCE.registerPotionRecipe(Potions.THICK, Ingredient.of(ModRegistry.ENDERSOUL_HAND_ITEM.get(), ModRegistry.HULK_HAMMER_ITEM.get(), ModRegistry.CREEPER_SHARD_ITEM.get(), ModRegistry.MUTANT_SKELETON_SKULL_ITEM.get()), ModRegistry.CHEMICAL_X_POTION.get());
    }

    private static void registerMessages() {
        NETWORK.register(C2SCreeperMinionTrackerMessage.class, C2SCreeperMinionTrackerMessage::new, MessageDirection.TO_SERVER);
        NETWORK.register(S2CMutantLevelParticlesMessage.class, S2CMutantLevelParticlesMessage::new, MessageDirection.TO_CLIENT);
        NETWORK.register(S2CAnimationMessage.class, S2CAnimationMessage::new, MessageDirection.TO_CLIENT);
        NETWORK.register(S2CSeismicWaveFluidParticlesMessage.class, S2CSeismicWaveFluidParticlesMessage::new, MessageDirection.TO_CLIENT);
        NETWORK.register(S2CMutantEndermanHeldBlockMessage.class, S2CMutantEndermanHeldBlockMessage::new, MessageDirection.TO_CLIENT);
    }

    @Override
    public void onEntityAttributeCreation(EntityAttributesCreateContext context) {
        context.registerEntityAttributes(ModRegistry.CREEPER_MINION_ENTITY_TYPE.get(), CreeperMinionEntity.registerAttributes());
        context.registerEntityAttributes(ModRegistry.ENDERSOUL_CLONE_ENTITY_TYPE.get(), EndersoulCloneEntity.registerAttributes());
        context.registerEntityAttributes(ModRegistry.MUTANT_CREEPER_ENTITY_TYPE.get(), MutantCreeperEntity.registerAttributes());
        context.registerEntityAttributes(ModRegistry.MUTANT_ENDERMAN_ENTITY_TYPE.get(), MutantEndermanEntity.registerAttributes());
        context.registerEntityAttributes(ModRegistry.MUTANT_SNOW_GOLEM_ENTITY_TYPE.get(), MutantSnowGolemEntity.registerAttributes());
        context.registerEntityAttributes(ModRegistry.SPIDER_PIG_ENTITY_TYPE.get(), SpiderPigEntity.registerAttributes());
        if (!ModLoaderEnvironment.INSTANCE.getModLoader().isForge()) {
            context.registerEntityAttributes(ModRegistry.MUTANT_SKELETON_ENTITY_TYPE.get(), MutantSkeletonEntity.registerAttributes());
            context.registerEntityAttributes(ModRegistry.MUTANT_ZOMBIE_ENTITY_TYPE.get(), MutantZombieEntity.registerAttributes());
        }
    }

    @Override
    public void onRegisterSpawnPlacements(SpawnPlacementsContext context) {
        context.registerSpawnPlacement(ModRegistry.CREEPER_MINION_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
        context.registerSpawnPlacement(ModRegistry.ENDERSOUL_CLONE_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        context.registerSpawnPlacement(ModRegistry.MUTANT_CREEPER_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        context.registerSpawnPlacement(ModRegistry.MUTANT_ENDERMAN_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MutantEndermanEntity::canSpawn);
        context.registerSpawnPlacement(ModRegistry.MUTANT_SKELETON_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        context.registerSpawnPlacement(ModRegistry.MUTANT_SNOW_GOLEM_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
        context.registerSpawnPlacement(ModRegistry.MUTANT_ZOMBIE_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        context.registerSpawnPlacement(ModRegistry.SPIDER_PIG_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Animal::checkAnimalSpawnRules);
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static ResourceLocation entityTexture(String name) {
        return id("textures/entity/" + name + ".png");
    }
}
