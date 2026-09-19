package com.MutantBattle.config;

public final class constants {

	private constants() {
	}

	public static final int MIN_TEAM_SIZE = 3;
	public static final int MAX_TEAM_SIZE = 11;
	public static final int DEFAULT_TEAM_SIZE = MIN_TEAM_SIZE;
	public static final int DEMO_TEAM_SIZE = 5;

	public static final int INITIAL_ENERGY = 100;
	public static final int MIN_DEFENSE_CAPACITY = 1;
	public static final int MAX_DEFENSE_CAPACITY = 3;
	public static final int MIN_POWER_DAMAGE = 1;
	public static final int MAX_POWER_DAMAGE = 3;
	public static final int MAX_POWER_LEVEL = 7;

	public static final int BATTLEFIELD_WIDTH = 800;
	public static final int BATTLEFIELD_HEIGHT = 600;
	public static final int MUTANT_SPEED = 5;
	public static final int ENCOUNTER_RADIUS = 25;
	public static final int REFRESH_RATE_MILLISECONDS = 100;
	public static final long MOVEMENT_STEP_INTERVAL_MILLISECONDS = 200;
	public static final int ENCOUNTER_POOL_SIZE = 4;
}