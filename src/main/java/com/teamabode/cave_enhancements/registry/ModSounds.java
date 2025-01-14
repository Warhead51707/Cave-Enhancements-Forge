package com.teamabode.cave_enhancements.registry;

import com.teamabode.cave_enhancements.CaveEnhancements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CaveEnhancements.MOD_ID);

    // Glow Paste
    public static RegistryObject<SoundEvent> ITEM_GLOW_PASTE_PLACE = registerSoundEvent("item.glow_paste.place");
    public static RegistryObject<SoundEvent> ITEM_AMETHYST_FLUTE_USE = registerSoundEvent("item.amethyst_flute.use");

    // Goop Blocks
    public static RegistryObject<SoundEvent> BLOCK_GOOP_BLOCK_BREAK = registerSoundEvent("block.goop_block.break");
    public static RegistryObject<SoundEvent> BLOCK_GOOP_BLOCK_STEP = registerSoundEvent("block.goop_block.step");
    public static RegistryObject<SoundEvent> BLOCK_GOOP_BLOCK_HIT = registerSoundEvent("block.goop_block.hit");
    public static RegistryObject<SoundEvent> BLOCK_GOOP_BLOCK_PLACE = registerSoundEvent("block.goop_block.place");
    public static RegistryObject<SoundEvent> BLOCK_GOOP_BLOCK_FALL = registerSoundEvent("block.goop_block.fall");
    public static RegistryObject<SoundEvent> BLOCK_GOOP_BLOCK_SLIDE = registerSoundEvent("block.goop_block.slide");
    public static RegistryObject<SoundEvent> BLOCK_GOOP_DECORATION_BREAK = registerSoundEvent("block.goop_decoration.break");
    public static RegistryObject<SoundEvent> BLOCK_GOOP_DECORATION_STEP = registerSoundEvent("block.goop_decoration.step");
    public static RegistryObject<SoundEvent> BLOCK_GOOP_DECORATION_HIT = registerSoundEvent("block.goop_decoration.hit");
    public static RegistryObject<SoundEvent> BLOCK_GOOP_DECORATION_PLACE = registerSoundEvent("block.goop_decoration.place");
    public static RegistryObject<SoundEvent> BLOCK_GOOP_DECORATION_FALL = registerSoundEvent("block.goop_decoration.fall");

    // Goop Sounds
    public static RegistryObject<SoundEvent> ENTITY_GOOP_DEATH = registerSoundEvent("entity.goop.death");
    public static RegistryObject<SoundEvent> ENTITY_GOOP_HURT = registerSoundEvent("entity.goop.hurt");
    public static RegistryObject<SoundEvent> ITEM_GOOP_THROW = registerSoundEvent("item.goop.throw");

    // Cruncher Sounds
    public static RegistryObject<SoundEvent> ENTITY_CRUNCHER_HURT = registerSoundEvent("entity.cruncher.hurt");
    public static RegistryObject<SoundEvent> ENTITY_CRUNCHER_DEATH = registerSoundEvent("entity.cruncher.death");
    public static RegistryObject<SoundEvent> ENTITY_CRUNCHER_STEP = registerSoundEvent("entity.cruncher.step");
    public static RegistryObject<SoundEvent> ENTITY_CRUNCHER_IDLE = registerSoundEvent("entity.cruncher.idle");

    // Glow Berry Juice
    public static RegistryObject<SoundEvent> ITEM_GLOW_BERRY_JUICE_DRINK = registerSoundEvent("item.glow_berry_juice.drink");

    // Dripstone Tortoise Sounds
    public static RegistryObject<SoundEvent> ENTITY_DRIPSTONE_TORTOISE_HURT = registerSoundEvent("entity.dripstone_tortoise.hurt");
    public static RegistryObject<SoundEvent> ENTITY_DRIPSTONE_TORTOISE_DEATH = registerSoundEvent("entity.dripstone_tortoise.death");
    public static RegistryObject<SoundEvent> ENTITY_DRIPSTONE_TORTOISE_STEP = registerSoundEvent("entity.dripstone_tortoise.step");
    public static RegistryObject<SoundEvent> ENTITY_DRIPSTONE_TORTOISE_IDLE = registerSoundEvent("entity.dripstone_tortoise.idle");
    public static RegistryObject<SoundEvent> ENTITY_DRIPSTONE_TORTOISE_BABY_DEATH = registerSoundEvent("entity.dripstone_tortoise.baby_death");
    public static RegistryObject<SoundEvent> ENTITY_DRIPSTONE_TORTOISE_BABY_HURT = registerSoundEvent("entity.dripstone_tortoise.baby_hurt");
    public static RegistryObject<SoundEvent> ENTITY_DRIPSTONE_TORTOISE_HATCH_EGG = registerSoundEvent("entity.dripstone_tortoise.hatch_egg");
    public static RegistryObject<SoundEvent> ENTITY_DRIPSTONE_TORTOISE_BREAK_EGG = registerSoundEvent("entity.dripstone_tortoise.break_egg");
    public static RegistryObject<SoundEvent> ENTITY_DRIPSTONE_TORTOISE_CRACK_EGG = registerSoundEvent("entity.dripstone_tortoise.crack_egg");


    // Misc
    public static RegistryObject<SoundEvent> ITEM_BUCKET_FILL_GOOP = registerSoundEvent("item.bucket.fill.goop");
    public static RegistryObject<SoundEvent> ITEM_BUCKET_EMPTY_GOOP = registerSoundEvent("item.bucket.empty.goop");
    public static RegistryObject<SoundEvent> BLOCK_ROSE_QUARTZ_CHIMES_CHIME = registerSoundEvent("block.rose_quartz_chimes.chime");
    public static RegistryObject<SoundEvent> EFFECT_REVERSAL_REVERSE = registerSoundEvent("effect.reversal.reverse");

    // Music
    public static final RegistryObject<SoundEvent> MUSIC_BIOME_GOOP_CAVES = registerSoundEvent("music.biome.goop_caves");
    public static final RegistryObject<SoundEvent> MUSIC_BIOME_ROSE_QUARTZ_CAVES = registerSoundEvent("music.biome.rose_quartz_caves");

    // Block Sound Types
    public static final ForgeSoundType GOOP_BLOCK = new ForgeSoundType(1.0F, 1.0F, BLOCK_GOOP_BLOCK_BREAK, BLOCK_GOOP_BLOCK_STEP, BLOCK_GOOP_BLOCK_PLACE, BLOCK_GOOP_BLOCK_HIT, BLOCK_GOOP_BLOCK_FALL);
    public static final ForgeSoundType GOOP_DECORATION = new ForgeSoundType(1.0F, 1.0F, BLOCK_GOOP_DECORATION_BREAK, BLOCK_GOOP_DECORATION_STEP, BLOCK_GOOP_DECORATION_PLACE, BLOCK_GOOP_DECORATION_HIT, BLOCK_GOOP_DECORATION_FALL);


    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(CaveEnhancements.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}

