package lab2
import chisel3._

class Trigger extends Module {
    val io = IO(new Bundle{
        val push_btn = Input(Bool())
        val trigger = Output(Bool())
    })

    io.trigger := io.push_btn & ~RegNext(io.push_btn)
}