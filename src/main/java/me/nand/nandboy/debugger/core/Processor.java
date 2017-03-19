/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.nand.nandboy.debugger.core;

import cpu.registers.Register;
import java.util.HashSet;
import java.util.Set;
import memory.MemoryMap;

public class Processor extends cpu.Processor {

	private final String BYTE_FORMAT = "0x%02X";

	private Set<Observer> observers;

	public Processor(MemoryMap memoryMap) {
		super(memoryMap);

		observers = new HashSet();
	}

	public void addObserver(Observer observer) {
		observers.add(observer);
	}

	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}

	@Override
	protected void LD_hh(Register dest, Register src) {
		super.LD_hh(dest, src);
		observers.forEach(o -> o.updateRegisters(this));
	}

	public String getA() {
		return String.format(BYTE_FORMAT, AF.getHighByte());
	}

	public String getF() {
		return String.format(BYTE_FORMAT, AF.getLowByte());
	}

	public void changeAF() {
		AF.setLowByte((byte) 0xFF);
		update();
	}

	public void update() {
		observers.forEach(o -> o.updateRegisters(this));
	}
}
