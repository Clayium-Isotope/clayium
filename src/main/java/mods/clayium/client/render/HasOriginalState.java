package mods.clayium.client.render;

import mods.clayium.util.UsedFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@UsedFor(UsedFor.Type.Block)
public @interface HasOriginalState {}
