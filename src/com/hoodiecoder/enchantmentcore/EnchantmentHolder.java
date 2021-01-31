package com.hoodiecoder.enchantmentcore;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentHolder {
private final List<CustomEnch> holder;
public EnchantmentHolder() {
	holder = new ArrayList<>();
}
boolean addEnchant(CustomEnch ench) {
	if (!holder.contains(ench)) {
		holder.add(ench);
		return true;
	} else {
		return false;
	}
}
boolean removeEnchant(CustomEnch ench) {
	if (holder.contains(ench)) {
		holder.remove(ench);
		return true;
	} else {
		return false;
	}
}
public void registerPendingEnchants() {
	for (CustomEnch cew : holder) {
		if (!EnchantmentCore.getInstance().getEnchList().contains(cew)) {
			EnchantmentCore.getInstance().addEnchant(cew);
		}
	}
}
}
