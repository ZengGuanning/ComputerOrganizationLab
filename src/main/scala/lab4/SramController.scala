package lab4
import chisel3._
import chisel3.util._

class SRAMController (
    DATA_WIDTH: Int = 32,
    ADDR_WIDTH: Int = 32,
    SRAM_ADDR_WIDTH: Int = 20,
    SRAM_DATA_WIDTH: Int = 32
) extends Module {
    val SRAM_BYTES = SRAM_DATA_WIDTH / 8
    val SRAM_BYTE_WIDTH = log2Ceil(SRAM_BYTES)
    val io = IO(new Bundle {
        // wishbone slave interface
        val wb_cyc_i = Input(Bool())
        val wb_stb_i = Input(Bool())
        val wb_ack_o = Output(Bool())
        val wb_adr_i = Input(UInt(ADDR_WIDTH.W))
        val wb_dat_i = Input(UInt(DATA_WIDTH.W))
        val wb_dat_o = Output(UInt(DATA_WIDTH.W))
        val wb_sel_i = Input(UInt((DATA_WIDTH / 8).W))
        val wb_we_i = Input(Bool())

        // sram interface
        val sram_addr = Output(UInt(SRAM_ADDR_WIDTH.W))
        val sram_dwrite = Output(UInt(SRAM_DATA_WIDTH.W))
        val sram_dread = Input(UInt(SRAM_DATA_WIDTH.W))
        val sram_ce_n = Output(Bool())
        val sram_oe_n = Output(Bool())
        val sram_we_n = Output(Bool())
        val sram_be_n = Output(UInt((SRAM_DATA_WIDTH / 8).W))
    })

    val addr_offset = RegInit(0.U(SRAM_ADDR_WIDTH.W))
    val ce = RegInit(true.B)
    val be = RegInit(0.U((SRAM_DATA_WIDTH / 8).W))
    val oe = RegInit(true.B)
    val dwrite = RegInit(0.U(SRAM_DATA_WIDTH.W))
    val dread = RegInit(0.U(SRAM_DATA_WIDTH.W))
    val idle::read1::read2::write1::write2::write3::done::Nil = Enum(7)
    val state = RegInit(idle)

    io.sram_addr := addr_offset
    io.sram_oe_n := oe
    io.sram_ce_n := ce
    io.sram_we_n := true.B
    io.sram_be_n := be
    io.sram_dwrite := dwrite
    io.wb_ack_o := false.B
    io.wb_dat_o := dread

    switch(state){
        is(idle){
            io.sram_addr := 0.U
            io.sram_oe_n := true.B
            io.sram_ce_n := true.B
            io.sram_we_n := true.B
            be := ~io.wb_sel_i
            io.wb_ack_o := false.B
            when(io.wb_cyc_i & io.wb_stb_i){
                when(io.wb_we_i){
                    state := write1
                    ce := false.B
                    addr_offset := io.wb_adr_i(SRAM_ADDR_WIDTH - 1 + SRAM_BYTE_WIDTH, SRAM_BYTE_WIDTH)
                    dwrite := io.wb_dat_i
                }.otherwise{
                    state := read1
                    addr_offset := io.wb_adr_i(SRAM_ADDR_WIDTH - 1 + SRAM_BYTE_WIDTH, SRAM_BYTE_WIDTH)
                    ce := false.B
                    oe := false.B
                }
            }.otherwise{
                state := idle
            }
        }
        is(read1){
            state := read2
            io.sram_we_n := true.B
            dread := io.sram_dread
        }
        is(read2){
            state := done
        }
        is(write1){
            state := write2
            io.sram_oe_n := true.B
            io.sram_we_n := true.B
            io.sram_be_n := ~io.wb_sel_i
        }
        is(write2){
            state := write3
            io.sram_we_n := false.B
        }
        is(write3){
            state := done
            io.sram_we_n := true.B
        }
        is(done){
            state := idle
            ce := true.B
            oe := true.B
            io.sram_we_n := true.B
            io.wb_ack_o := true.B
        }
    }
}