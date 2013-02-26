package net.azulite.sampleshooting;

import java.util.Iterator;
import java.util.LinkedList;

import net.azulite.Amanatsu.*;

// assetsからの素材生成例。
public class Game extends GameView
{
  // 全体で使う拡大率。
  // これをベースにキャラの大きさなどを決める。
  // 画面の大きさを変更するよりも、高解像度の端末で粗さが出てこない。
  // なお、この拡大率は高さを元に計算している。
  float scale;

  // 自機。
  Player player;

  // 敵。
  Object enemy[];
  // 敵の最大数。
  static final int ENEMY_MAX = 10;

  // ショット。
  static Shot shot[], dshot;
  // ショットの最大数。100くらいあればいいだろ。
  static final int SHOT_MAX = 100;
  // 生きてるショットのリスト。
  // 生きてるショットを効率よく保存して、判定式とか簡素化しようかと。
  static LinkedList <Shot> liveshot;
  // Shotのイテレーター。
  Iterator<Shot> shotit;
  static int search;

  // 可能な限りGCとか避けたいので、ループ変数もこちらで定義しておく。
  int max, i;

  @Override
  public void UserInit()
  {
    // Assets内にある画像ファイルは、リサイズされない。
    draw.createTexture( 0, "material.png" );

    scale = draw.getHeight() / 5.0f / 128.0f;

    // 自機の生成。
    player = new Player( scale * 1.5f );
    player.set( 10, 526.0f * scale / 128.0f, draw.getHeight() / 2.0f );

    // 敵の生成。
    enemy = new Object[ ENEMY_MAX ];
    for ( i = 0 ; i < ENEMY_MAX ; ++i )
    {
      // とりあえず敵1のオブジェクトを作っておく。
      enemy[ i ] = new Enemy1( scale );
    }

    // ショットの生成。
    shot = new Shot[ SHOT_MAX ];
    for ( i = 0 ; i < SHOT_MAX ; ++i )
    {
      shot[ i ] = new Shot( scale );
    }
    liveshot = new LinkedList<Shot>();
  }

  @Override
  public boolean MainLoop()
  {
    // 自機移動。
    player.move();

    // 敵移動。
    for ( i = 0 ; i < ENEMY_MAX ; ++i )
    {
      enemy[ i ].move();
    }

    // ショット移動。
    shotit = liveshot.iterator();
    while ( shotit.hasNext() )
    {
      dshot = shotit.next();
      dshot.move();
      if ( dshot.isDead() ){ shotit.remove(); search = 0; }
    }

    // 画面初期化。
    draw.clearScreen();

    // FPSの表示。
    draw.printf( 0, 0, 0, "FPS:" + draw.getFps() +":"+player.dismove[0]+","+player.dismove[1]+"max:"+player.max+"shotmax:"+liveshot.size()+"search:"+search);

    // 自機描画。
    player.draw();

    // 敵描画。
    for ( i = 0 ; i < ENEMY_MAX ; ++i )
    {
      enemy[ i ].draw();
    }

    // ショット描画。
    shotit = liveshot.iterator();
    while ( shotit.hasNext() )
    {
      shotit.next().draw();
    }

    return true;
  }

  // ショット管理。
  static boolean addShot( int hp, float sx, float sy, int type, float speed, float rad )
  {
    for ( ; search < SHOT_MAX ; ++search )
    {
      if ( shot[ search ].isDead() )
      {
        shot[ search ].set( hp, sx, sy, type, speed, rad );
        liveshot.add( shot[ search++ ] );
        return true;
      }
    }
    return false;
  }
}

class Shot extends Object
{
  float speed, rad;
  static int W, H;

  public Shot( float scale )
  {
    super( 0, 0, 32, 32, scale );
    W = Game.draw.getWidth();
    H = Game.draw.getHeight();
  }

  public void set( int hp, float sx, float sy, int type, float speed, float rad )
  {
    this.set( hp, sx, sy );
    this.speed = speed;
    this.rad = rad;
    //ry = 128+ type * 32;
    rx = 0;
    ry = 128;
  }

  public void move()
  {
    // 移動。
    x += Math.cos( rad ) * speed;
    y += Math.sin( rad ) * speed;
    if ( x < 0 || W < x || y < 0 || H < y )//x < -32 * scale || y < -32 * scale || W < x + 32 * scale || H < y + 32 * scale )
    {
      // 領域外に出たので死亡。
      hp = 0;
    }
  }

  /*public boolean draw()
  {
    if ( isLive() )
    {
      Game.draw.drawBoxC( x, y, 32, 32, GameColor.RED );
      return true;
    } else if ( isExplosion() )
    {
      ++hp;
    }
    return false;
  }*/
}

class Player extends Object
{
  float dis, dismove[];
  float ix, iy;
  float speed;
  float[] prad;
  float rad;
  float moverad;
  int i, max, finger, shot;
  int lv;

  public Player( float scale ) {
    super( 0, 0, 128, 128, scale );
    dismove = new float[ 2 ];
    speed = 128.0f * scale / 10.0f;
    moverad = 128.0f * scale * 2.0f;
    dismove[ 0 ] = moverad * moverad;
    dismove[ 1 ] = moverad * moverad * 4.0f;
    prad = new float[ 3 ];
    lv = 3;
  }

  @Override
  public void move()
  {
    if ( Game.input.getTouchFrame() > 0 )
    {
      // どこか一箇所はタッチしている。

      finger = -1;

      // すべての指を調べる。
      max = Game.input.fingerNum();
      for ( i = 0 ; i < max ; ++i )
      {
        ix = Game.input.getX( i );
        iy = Game.input.getY( i );
        dis = this.distance( ix, iy );
        if ( dis < dismove[ 1 ] )
        {
          // 移動範囲内に指がある。
          // 移動範囲がそこそこ小さいので、指が複数来ることは想定しない。
          finger = i;
          if ( dis < dismove[ 0 ] )
          {
            // 近い場所にいるので、その場所に移動させる。
            x = ix;
            y = iy;
          } else
          {
            // 移動開始。
            rad = (float)Math.atan2( iy - y, ix - x );
            x += Math.cos( rad ) * speed;
            y += Math.sin( rad ) * speed;
          }
          break;
        }
      }

      // ショットを撃つ。
      if ( max > 1 || finger < 0 )
      {
        // 指の数が1本より多いか、移動に使われた指がない場合にショットを撃つ。
        // 指は必ず1本以上はある。
        shot = 0;
        for ( i = 0 ; i < max ; ++i )
        {
          if ( finger != i )
          {
            prad[ shot ] =  (float)Math.atan2( Game.input.getY( i ) - y, Game.input.getX( i ) - x );
            Game.addShot( 1, x + scale * 32.0f, y, 0, scale * speed, prad[ shot ] );
            ++shot;
            if ( shot >= lv ){ break; }
          }
        }
      }

    }

  }

  // 描画処理。
  // 若干前に描画する。
  public boolean draw()
  {
    if ( isLive() )
    {
      for ( i = 0 ; i < lv ; ++i )
      {
        Game.draw.drawTextureScaleRotateC( 0, rx, ry, w, h, x + scale * 32.0f, y, scale, (float)Math.PI / 4.0f + prad[ i ] );
      }
      Game.draw.drawCircleLineC( x, y, moverad, GameColor.YELLOW, 2.0f );
      return true;
    } else if ( isExplosion() )
    {
      ++hp;
    }
    return false;
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
