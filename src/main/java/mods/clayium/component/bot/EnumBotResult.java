package mods.clayium.component.bot;

/**
 * bot内部/プロバイダ/外界、不足・過剰、継続可否あたりで整理したい
 */
public enum EnumBotResult {
    Success,
    NotReady,
    ProgressLack,
    Obstacle,
    Overloading,
    Incomplete,
    EndOfTerm
}
