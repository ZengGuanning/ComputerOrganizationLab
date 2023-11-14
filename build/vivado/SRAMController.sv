// Generated by CIRCT firtool-1.58.0
// Standard header to adapt well known macros to our needs.
`ifndef RANDOMIZE
  `ifdef RANDOMIZE_REG_INIT
    `define RANDOMIZE
  `endif // RANDOMIZE_REG_INIT
`endif // not def RANDOMIZE

// RANDOM may be set to an expression that produces a 32-bit random unsigned value.
`ifndef RANDOM
  `define RANDOM $random
`endif // not def RANDOM

// Users can define INIT_RANDOM as general code that gets injected into the
// initializer block for modules with registers.
`ifndef INIT_RANDOM
  `define INIT_RANDOM
`endif // not def INIT_RANDOM

// If using random initialization, you can also define RANDOMIZE_DELAY to
// customize the delay used, otherwise 0.002 is used.
`ifndef RANDOMIZE_DELAY
  `define RANDOMIZE_DELAY 0.002
`endif // not def RANDOMIZE_DELAY

// Define INIT_RANDOM_PROLOG_ for use in our modules below.
`ifndef INIT_RANDOM_PROLOG_
  `ifdef RANDOMIZE
    `ifdef VERILATOR
      `define INIT_RANDOM_PROLOG_ `INIT_RANDOM
    `else  // VERILATOR
      `define INIT_RANDOM_PROLOG_ `INIT_RANDOM #`RANDOMIZE_DELAY begin end
    `endif // VERILATOR
  `else  // RANDOMIZE
    `define INIT_RANDOM_PROLOG_
  `endif // RANDOMIZE
`endif // not def INIT_RANDOM_PROLOG_

// Include register initializers in init blocks unless synthesis is set
`ifndef SYNTHESIS
  `ifndef ENABLE_INITIAL_REG_
    `define ENABLE_INITIAL_REG_
  `endif // not def ENABLE_INITIAL_REG_
`endif // not def SYNTHESIS

// Include rmemory initializers in init blocks unless synthesis is set
`ifndef SYNTHESIS
  `ifndef ENABLE_INITIAL_MEM_
    `define ENABLE_INITIAL_MEM_
  `endif // not def ENABLE_INITIAL_MEM_
`endif // not def SYNTHESIS

module SRAMController(	// <stdin>:3:3
  input         clock,	// <stdin>:4:11
                reset,	// <stdin>:5:11
                io_wb_cyc_i,	// src/main/scala/lab4/SramController.scala:13:16
                io_wb_stb_i,	// src/main/scala/lab4/SramController.scala:13:16
  output        io_wb_ack_o,	// src/main/scala/lab4/SramController.scala:13:16
  input  [31:0] io_wb_adr_i,	// src/main/scala/lab4/SramController.scala:13:16
                io_wb_dat_i,	// src/main/scala/lab4/SramController.scala:13:16
  output [31:0] io_wb_dat_o,	// src/main/scala/lab4/SramController.scala:13:16
  input  [3:0]  io_wb_sel_i,	// src/main/scala/lab4/SramController.scala:13:16
  input         io_wb_we_i,	// src/main/scala/lab4/SramController.scala:13:16
  output [19:0] io_sram_addr,	// src/main/scala/lab4/SramController.scala:13:16
  output [31:0] io_sram_dwrite,	// src/main/scala/lab4/SramController.scala:13:16
  input  [31:0] io_sram_dread,	// src/main/scala/lab4/SramController.scala:13:16
  output        io_sram_ce_n,	// src/main/scala/lab4/SramController.scala:13:16
                io_sram_oe_n,	// src/main/scala/lab4/SramController.scala:13:16
                io_sram_we_n,	// src/main/scala/lab4/SramController.scala:13:16
  output [3:0]  io_sram_be_n	// src/main/scala/lab4/SramController.scala:13:16
);

  reg  [19:0] addr_offset;	// src/main/scala/lab4/SramController.scala:34:30
  reg         ce;	// src/main/scala/lab4/SramController.scala:35:21
  reg  [3:0]  be;	// src/main/scala/lab4/SramController.scala:36:21
  reg         oe;	// src/main/scala/lab4/SramController.scala:37:21
  reg  [31:0] dwrite;	// src/main/scala/lab4/SramController.scala:38:25
  reg  [31:0] dread;	// src/main/scala/lab4/SramController.scala:39:24
  reg  [2:0]  state;	// src/main/scala/lab4/SramController.scala:41:24
  wire        _GEN = state == 3'h0;	// src/main/scala/lab4/SramController.scala:41:24, :52:18
  wire        _GEN_0 = state == 3'h1;	// src/main/scala/lab4/SramController.scala:41:24, :52:18, :67:27
  wire        _GEN_1 = state == 3'h2;	// src/main/scala/lab4/SramController.scala:41:24, :52:18, :77:19
  wire        _GEN_2 = state == 3'h3;	// src/main/scala/lab4/SramController.scala:41:24, :52:18, :62:27
  wire        _GEN_3 = _GEN_0 | _GEN_1;	// src/main/scala/lab4/SramController.scala:44:18, :52:18
  wire        _GEN_4 = state == 3'h4;	// src/main/scala/lab4/SramController.scala:41:24, :52:18, :85:19
  wire        _GEN_5 = state == 3'h6;	// src/main/scala/lab4/SramController.scala:41:24, :52:18, :82:19
  wire        _GEN_6 = _GEN_0 | _GEN_1 | _GEN_2 | _GEN_4 | state == 3'h5;	// src/main/scala/lab4/SramController.scala:35:21, :41:24, :52:18, :91:19
  always @(posedge clock) begin	// <stdin>:4:11
    if (reset) begin	// <stdin>:4:11
      addr_offset <= 20'h0;	// src/main/scala/lab4/SramController.scala:34:30
      ce <= 1'h1;	// src/main/scala/lab4/SramController.scala:35:21
      be <= 4'h0;	// src/main/scala/lab4/SramController.scala:36:21
      oe <= 1'h1;	// src/main/scala/lab4/SramController.scala:35:21, :37:21
      dwrite <= 32'h0;	// src/main/scala/lab4/SramController.scala:38:25
      dread <= 32'h0;	// src/main/scala/lab4/SramController.scala:38:25, :39:24
      state <= 3'h0;	// src/main/scala/lab4/SramController.scala:41:24
    end
    else begin	// <stdin>:4:11
      automatic logic            _GEN_7;	// src/main/scala/lab4/SramController.scala:60:30
      automatic logic [7:0][2:0] _GEN_8;	// src/main/scala/lab4/SramController.scala:41:24, :52:18, :60:44, :77:19, :82:19, :85:19, :91:19, :95:19, :99:19
      _GEN_7 = io_wb_cyc_i & io_wb_stb_i;	// src/main/scala/lab4/SramController.scala:60:30
      if (_GEN & _GEN_7)	// src/main/scala/lab4/SramController.scala:34:30, :52:18, :60:{30,44}, :61:33
        addr_offset <= io_wb_we_i ? io_wb_adr_i[21:2] : io_wb_adr_i[21:2];	// src/main/scala/lab4/SramController.scala:34:30, :61:33, :64:{33,47}, :68:{33,47}
      if (_GEN) begin	// src/main/scala/lab4/SramController.scala:52:18
        ce <= ~_GEN_7 & ce;	// src/main/scala/lab4/SramController.scala:35:21, :60:{30,44}, :61:33
        be <= ~io_wb_sel_i;	// src/main/scala/lab4/SramController.scala:36:21, :58:19
        oe <= (~_GEN_7 | io_wb_we_i) & oe;	// src/main/scala/lab4/SramController.scala:35:21, :37:21, :60:{30,44}, :61:33
      end
      else begin	// src/main/scala/lab4/SramController.scala:52:18
        automatic logic _GEN_9 = ~_GEN_6 & _GEN_5;	// src/main/scala/lab4/SramController.scala:35:21, :52:18
        ce <= _GEN_9 | ce;	// src/main/scala/lab4/SramController.scala:35:21, :52:18
        oe <= _GEN_9 | oe;	// src/main/scala/lab4/SramController.scala:35:21, :37:21, :52:18
      end
      if (_GEN & _GEN_7 & io_wb_we_i)	// src/main/scala/lab4/SramController.scala:38:25, :52:18, :60:{30,44}, :61:33, :65:28
        dwrite <= io_wb_dat_i;	// src/main/scala/lab4/SramController.scala:38:25
      if (_GEN | ~_GEN_0) begin	// src/main/scala/lab4/SramController.scala:39:24, :52:18
      end
      else	// src/main/scala/lab4/SramController.scala:39:24, :52:18
        dread <= io_sram_dread;	// src/main/scala/lab4/SramController.scala:39:24
      _GEN_8 =
        {{state},
         {3'h0},
         {3'h6},
         {3'h5},
         {3'h4},
         {3'h6},
         {3'h2},
         {_GEN_7 ? {1'h0, io_wb_we_i, 1'h1} : 3'h0}};	// src/main/scala/lab4/SramController.scala:35:21, :41:24, :49:17, :52:18, :60:{30,44}, :61:33, :62:27, :67:27, :73:23, :77:19, :82:19, :85:19, :91:19, :95:19, :99:19
      state <= _GEN_8[state];	// src/main/scala/lab4/SramController.scala:41:24, :52:18, :60:44, :77:19, :82:19, :85:19, :91:19, :95:19, :99:19
    end
  end // always @(posedge)
  `ifdef ENABLE_INITIAL_REG_	// <stdin>:3:3
    `ifdef FIRRTL_BEFORE_INITIAL	// <stdin>:3:3
      `FIRRTL_BEFORE_INITIAL	// <stdin>:3:3
    `endif // FIRRTL_BEFORE_INITIAL
    initial begin	// <stdin>:3:3
      automatic logic [31:0] _RANDOM[0:2];	// <stdin>:3:3
      `ifdef INIT_RANDOM_PROLOG_	// <stdin>:3:3
        `INIT_RANDOM_PROLOG_	// <stdin>:3:3
      `endif // INIT_RANDOM_PROLOG_
      `ifdef RANDOMIZE_REG_INIT	// <stdin>:3:3
        for (logic [1:0] i = 2'h0; i < 2'h3; i += 2'h1) begin
          _RANDOM[i] = `RANDOM;	// <stdin>:3:3
        end	// <stdin>:3:3
        addr_offset = _RANDOM[2'h0][19:0];	// <stdin>:3:3, src/main/scala/lab4/SramController.scala:34:30
        ce = _RANDOM[2'h0][20];	// <stdin>:3:3, src/main/scala/lab4/SramController.scala:34:30, :35:21
        be = _RANDOM[2'h0][24:21];	// <stdin>:3:3, src/main/scala/lab4/SramController.scala:34:30, :36:21
        oe = _RANDOM[2'h0][25];	// <stdin>:3:3, src/main/scala/lab4/SramController.scala:34:30, :37:21
        dwrite = {_RANDOM[2'h0][31:26], _RANDOM[2'h1][25:0]};	// <stdin>:3:3, src/main/scala/lab4/SramController.scala:34:30, :38:25
        dread = {_RANDOM[2'h1][31:26], _RANDOM[2'h2][25:0]};	// <stdin>:3:3, src/main/scala/lab4/SramController.scala:38:25, :39:24
        state = _RANDOM[2'h2][28:26];	// <stdin>:3:3, src/main/scala/lab4/SramController.scala:39:24, :41:24
      `endif // RANDOMIZE_REG_INIT
    end // initial
    `ifdef FIRRTL_AFTER_INITIAL	// <stdin>:3:3
      `FIRRTL_AFTER_INITIAL	// <stdin>:3:3
    `endif // FIRRTL_AFTER_INITIAL
  `endif // ENABLE_INITIAL_REG_
  assign io_wb_ack_o = ~(_GEN | _GEN_6) & _GEN_5;	// <stdin>:3:3, src/main/scala/lab4/SramController.scala:35:21, :49:17, :52:18, :59:25
  assign io_wb_dat_o = dread;	// <stdin>:3:3, src/main/scala/lab4/SramController.scala:39:24
  assign io_sram_addr = _GEN ? 20'h0 : addr_offset;	// <stdin>:3:3, src/main/scala/lab4/SramController.scala:34:30, :43:18, :52:18, :54:26
  assign io_sram_dwrite = dwrite;	// <stdin>:3:3, src/main/scala/lab4/SramController.scala:38:25
  assign io_sram_ce_n = _GEN | ce;	// <stdin>:3:3, src/main/scala/lab4/SramController.scala:35:21, :45:18, :52:18, :56:26
  assign io_sram_oe_n = _GEN | ~_GEN_3 & _GEN_2 | oe;	// <stdin>:3:3, src/main/scala/lab4/SramController.scala:37:21, :44:18, :52:18, :55:26
  assign io_sram_we_n = _GEN | _GEN_0 | _GEN_1 | _GEN_2 | ~_GEN_4;	// <stdin>:3:3, src/main/scala/lab4/SramController.scala:35:21, :46:18, :49:17, :52:18, :57:26, :78:26, :87:26, :92:26
  assign io_sram_be_n = _GEN | _GEN_3 | ~_GEN_2 ? be : ~io_wb_sel_i;	// <stdin>:3:3, src/main/scala/lab4/SramController.scala:36:21, :44:18, :47:18, :52:18, :88:29
endmodule

