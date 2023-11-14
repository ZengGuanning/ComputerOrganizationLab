package main

import chisel3._
import chisel3.util._

class Adder extends Module {
    val io = IO(new Bundle {
        val a_i = Input(UInt(4.W))
        val b_i = Input(UInt(4.W))
        val c_o = Output(UInt(4.W))
    })
    io.c_o := io.a_i + io.b_i
}