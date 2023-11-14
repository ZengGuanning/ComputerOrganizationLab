package main
import lab2._
import lab3._
import lab4._
import lab5._

object MakeVivadoVerilog {
  def main(args: Array[String]): Unit = {
    circt.stage.ChiselStage.emitSystemVerilogFile(new Lab2, Array("--target-dir", args(0)))
    circt.stage.ChiselStage.emitSystemVerilogFile(new Lab3, Array("--target-dir", args(0)))
    circt.stage.ChiselStage.emitSystemVerilogFile(new SRAMController, Array("--target-dir", args(0)))
    circt.stage.ChiselStage.emitSystemVerilogFile(new Lab5Master, Array("--target-dir", args(0)))
  }
}

object MakeVerilatorVerilog {
  def main(args: Array[String]): Unit = {
    circt.stage.ChiselStage.emitSystemVerilogFile(new Adder, Array("--target-dir", args(0)))
  }
}
