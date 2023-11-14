package lab3
import chisel3._
import chisel3.util._

class Lab3 extends Module {
    val io = IO(new Bundle{
        val push_btn = Input(Bool())
        val inst = Input(UInt(32.W))
        val leds = Output(UInt(16.W))
    })

    val trigger = Module(new Trigger)
    val controller = Module(new Controller)
    val regfile = Module(new RegFile)
    val alu = Module(new ALU)

    trigger.io.push_btn := io.push_btn
    controller.io.step := trigger.io.step

    controller.io.inst := io.inst
    io.leds := controller.io.leds

    controller.io_rf <> regfile.io
    controller.io_alu <> alu.io
}