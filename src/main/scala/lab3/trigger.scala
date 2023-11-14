package lab3
import chisel3._

class Trigger extends Module {
    val io = IO(new Bundle{
        val push_btn = Input(Bool())
        val step = Output(Bool())
    })

    io.step := io.push_btn & !RegNext(io.push_btn)
}