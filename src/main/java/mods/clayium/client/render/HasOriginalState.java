package mods.clayium.client.render;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import mods.clayium.util.UsedFor;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@UsedFor(UsedFor.Type.Block)
public @interface HasOriginalState {}
