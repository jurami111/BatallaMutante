package com.MutantBattle;

import com.MutantBattle.ui.UIMain;
import com.MutantBattle.model.ModelMain;
import com.MutantBattle.game.GameMain;
import com.MutantBattle.control.ControlMain;
//Aca se va a controlar la ejecucion principal del juego

public class Main{
    public static void main(String[] args){
        System.out.println("Welcome to the Mutant Battle!");
        UIMain.uiMain();
        ModelMain.modelMain();
        GameMain.gameMain();
        ControlMain.controlMain();
    }
}