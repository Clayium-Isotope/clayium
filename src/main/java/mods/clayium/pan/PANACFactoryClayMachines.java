package mods.clayium.pan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mods.clayium.block.ClayEnergyLaser;
import mods.clayium.block.laser.ClayLaser;
import mods.clayium.block.tile.TileCAInjector;
import mods.clayium.block.tile.TileCAReactor;
import mods.clayium.block.tile.TileClayEnergyLaser;
import mods.clayium.block.tile.TileClayMachines;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.crafting.IItemPattern;
import mods.clayium.util.crafting.Recipes;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class PANACFactoryClayMachines
        implements IPANAdapterConversionFactory {
    public boolean match(World world, int x, int y, int z) {
        return UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z) instanceof TileClayMachines;
    }


    public IPANConversion getConversion(IPANAdapter adapter) {
        boolean flag = true;
        for (ItemStack item : adapter.getPatternItems()) {
            if (item != null) {
                flag = false;
                break;
            }
        }
        if (flag) {
            return null;
        }
        TileEntity te = UtilBuilder.safeGetTileEntity((IBlockAccess) adapter.getConnectedWorld(), adapter.getConnectedXCoord(), adapter.getConnectedYCoord(), adapter.getConnectedZCoord());
        if (te instanceof TileClayMachines) {
            Recipes recipe = Recipes.GetRecipes(((TileClayMachines) te).getRecipeId());
            if (recipe != null) {
                List<ItemStack> ingred = new ArrayList<ItemStack>();
                for (ItemStack item : adapter.getPatternItems()) {
                    if (item != null) {
                        ingred.add(item);
                    }
                }
                Recipes.RecipeCondition condition = recipe.getRecipeConditionFromInventory(ingred.<ItemStack>toArray(new ItemStack[0]), 0, ((TileClayMachines) te).getRecipeTier());
                Recipes.RecipeResult result = (condition != null) ? recipe.getRecipeResult(condition) : null;
                if (result != null) {


                    List<IItemPattern> patterns = new ArrayList<IItemPattern>();

                    for (Object material : condition.getMaterials()) {
                        patterns.add(Recipes.convert(material));
                    }

                    List<ItemStack> results = new ArrayList<ItemStack>(Arrays.asList(result.getResults()));


                    long energy = (long) ((float) result.getEnergy() * ((TileClayMachines) te).multConsumingEnergy);
                    long time = (long) ((float) result.getTime() * ((TileClayMachines) te).multCraftTime);
                    if (te instanceof mods.clayium.block.tile.TileClayReactor) {
                        int b = 0, g = 0, r = 0;
                        for (ItemStack item : adapter.getSubItems()) {
                            if (item != null && item.getItem() instanceof ItemBlock) {
                                Block block = ((ItemBlock) item.getItem()).field_150939_a;
                                if (block instanceof ClayEnergyLaser)
                                    switch (((ClayEnergyLaser) block).getTier(item)) {
                                        case 7:
                                            b += item.stackSize;
                                            break;
                                        case 8:
                                            g += item.stackSize;
                                            break;
                                        case 9:
                                            r += item.stackSize;
                                            break;
                                    }

                            }
                        }
                        long le = (long) (ClayLaser.getEnergy(new int[] {b, g, r}) + 1.0D);
                        time--;
                        if (time <= 0L) time = 1L;
                        time = time / le + 1L;
                        energy += (TileClayEnergyLaser.consumingEnergyBlue * b);
                        energy += (TileClayEnergyLaser.consumingEnergyGreen * g);
                        energy += (TileClayEnergyLaser.consumingEnergyRed * r);
                    }
                    if (te instanceof TileCAReactor) {
                        if (((TileCAReactor) te).isConstructed() && ((TileCAReactor) te).getResultPureAntimatter() != null) {
                            energy = (long) (energy * ((TileCAReactor) te).getConsumingEnergyTotalMultiplier());
                            time = (long) (time * ((TileCAReactor) te).getCraftTimeTotalMultiplier());
                            results.clear();
                            results.add(((TileCAReactor) te).getResultPureAntimatter());
                        } else {
                            results.clear();
                        }
                    }
                    if (te instanceof TileCAInjector) {
                        time = (long) (time * ((TileCAInjector) te).getCraftTimeMultiplier());
                    }
                    if (time <= 0L) time = 1L;

                    if (results.size() > 0)
                        return new PANConversion(patterns.<IItemPattern>toArray(new IItemPattern[0]), results.<ItemStack>toArray(new ItemStack[0]), energy * time);
                }
            }
        }
        return null;
    }
}
