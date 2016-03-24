package net.rperce.compactstuff.blockcompact;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class BlockCompact extends Block {
    public static final String canonicalName = "blockcompact";

    public BlockCompact(Material m) {
        super(m);
    }
    public BlockCompact() {
        this(Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setStepSound(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 2);
    }

    public static ItemStack stack(Meta m) { return stack(1, m); }
    public static ItemStack stack(int amt, Meta m) { return new ItemStack(StartupCommon.compactBlock, amt, m.id);}

    public static final PropertyEnum<Meta> PROPERTY_NAME = PropertyEnum.create("name", Meta.class);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PROPERTY_NAME);
    }

    @Override
    public int damageDropped(IBlockState state) {
        Meta meta = state.getValue(PROPERTY_NAME);
        return meta.id;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (Meta m : Meta.values()) {
            list.add(new ItemStack(itemIn, 1, m.id));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(PROPERTY_NAME, Meta.fromID(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        Meta meta = state.getValue(PROPERTY_NAME);
        return meta.id;
    }

//    @Override
//    protected BlockState createBlockState() {
//        return new BlockState(this, PROPERTY_NAME);
//    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(PROPERTY_NAME, Meta.fromID(meta));
    }

    @Override
    public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
        return (world.getBlockState(pos).getValue(PROPERTY_NAME) == Meta.COMNETHER) || super.isFireSource(world, pos, side);
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return (blockState.getValue(PROPERTY_NAME)).hardness;
    }


    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        return getBlockHardness(world.getBlockState(pos), world, pos);
    }

    public enum Meta implements IStringSerializable {
        COMCOBBLE(0,  5),
        IRONSTONE(1,  20),
        COMNETHER(2,  1),
        COMDIAMOND(3, 14),
        COMIRON(4,    14),
        COMGOLD(5,    10),
        STEELBLOCK(6, 5),
        COMSTEEL(7,   14);


        public final int id, hardness;

        Meta(int id, int hardness) {
            this.hardness = hardness;
            this.id = id;
        }

        @Override
        public String getName() {
            return this.toString();
        }

        public static Meta fromID(int id) {
            if (id < 0 || id > Meta.values().length) {
                id = 0;
            }
            return Meta.values()[id];
        }

        public static Stream<String> getNames() {
            return Arrays.stream(Meta.values()).map(Meta::toString);
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
}