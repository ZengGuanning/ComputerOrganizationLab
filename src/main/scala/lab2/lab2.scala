package lab2
import chisel3._

// 你需要在这里实例化两个模块Trigger和Counter,接到一起

class Lab2 extends Module {
    val io = IO(new Bundle{
        val push_btn = Input(Bool())
        val count = Output(UInt(4.W))
    })

    val trigger = Module(new Trigger)
    val counter = Module(new Counter)

    trigger.io.push_btn := io.push_btn
    counter.io.trigger := trigger.io.trigger
    io.count := counter.io.count
}

