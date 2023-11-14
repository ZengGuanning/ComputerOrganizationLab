package lab2
import chisel3._
class Counter extends Module {
    val io = IO(new Bundle{
        val trigger = Input(Bool())
        val count = Output(UInt(4.W))
    })
    val count_reg = RegInit(0.U(4.W))
    when(io.trigger === 1.U){
        count_reg := Mux(count_reg === 15.U, 15.U, count_reg + 1.U)
    }
    io.count := count_reg
}