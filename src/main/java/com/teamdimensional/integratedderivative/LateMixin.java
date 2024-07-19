package com.teamdimensional.integratedderivative;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.LinkedList;
import java.util.List;

public class LateMixin implements ILateMixinLoader {
    @Override
    public List<String> getMixinConfigs() {
        List<String> list = new LinkedList<>();
        list.add("mixins.integratedderivative.json");
        return list;
    }
}
