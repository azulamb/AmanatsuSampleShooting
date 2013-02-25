package net.azulite.sampleshooting;

import net.azulite.Amanatsu.*;

public class Game extends GameView
{
  Player player;
  float scale;
  @Override
  public void UserInit()
  {
    // Assets内にある画像ファイルは、リサイズされない。
    draw.createTexture( 0, "material.png" );

    scale = draw.getHeight() / 10.0f / 128.0f;

    // 自機の生成。
    player = new Player( scale );
    player.set( 10, 526.0f * scale, draw.getHeight() / 2.0f );
  }

  @Override
  public boolean MainLoop()
  {
    // 自機移動。
    player.move();

    // 画面初期化。
    draw.clearScreen();

    // 自機描画。
    player.draw();

    return true;
  }
}

class Player extends Object
{
  float movedis;
  float ix, iy;
  float speed;

  public Player( float scale ) {
    super( 0, 0, 128, 128, scale );
    movedis = 2.0f * 2.0f;
    speed = 5.0f;
  }

  @Override
  public void move()
  {
    ix = Game.input.getX();
    iy = Game.input.getY();
    if ( Game.input.getTouchFrame() > 0 && this.distance( ix, iy ) > movedis )
    {
      // 移動開始。
      float rad = (float)Math.atan2( iy - y, ix - x );
      x += Math.cos( rad ) * speed;
      y += Math.sin( rad ) * speed;
    }
  }
}

class Enemy1 extends Object
{
  public Enemy1( float scale )
  {
    super( 128, 128, 128, 128, scale );
  }

}

class Enemy2 extends Object
{
  public Enemy2( float scale )
  {
    super( 256, 128, 128, 128, scale );
  }

}

class Enemy3 extends Object
{
  public Enemy3( float scale )
  {
    super( 384, 128, 128, 128, scale );
  }

}