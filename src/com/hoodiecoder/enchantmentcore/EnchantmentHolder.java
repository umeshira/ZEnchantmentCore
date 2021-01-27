package com.hoodiecoder.enchantmentcore;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentHolder {
private final List<CoreEnchWrapper> holder;
public EnchantmentHolder() {
	holder = new ArrayList<>();
}
void addEnchant(CoreEnchWrapper ench) {
	holder.add(ench);
}
boolean removeEnchant(CoreEnchWrapper ench) {
	if (holder.contains(ench)) {
		holder.remove(ench);
		return true;
	} else {
		return false;
	}
}
public void registerPendingEnchants() {
	for (CoreEnchWrapper cew : holder) {
		if (!EnchantmentCore.getInstance().getEnchList().contains(cew)) {
			EnchantmentCore.getInstance().addEnchant(cew);
		}
	}
}
}
