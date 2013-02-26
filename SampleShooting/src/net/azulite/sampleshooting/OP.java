package net.azulite.sampleshooting;

import net.azulite.Amanatsu.*;

// resからの素材生成の例。
public class OP extends GameView
{
  int timer = 0;
  float w, h, dw, dh;

  // 入力処理。
  // 自機のショット及び決定ボタン。
  public int getShotButton()
  {
    // タッチされてたらタッチフレーム数を返す。
    if ( input.getTouchFrame() > 0 ){ return input.getTouchFrame(); }
    // キーボードのスペースが押されていたら、スペースのフレーム数を返す。
    if ( input.getKey( AmanatsuKey.K_SPACE ) > 0 ){ return input.getKey( AmanatsuKey.K_SPACE ); }
    // ゲームパッドの1ボタンのフレーム数を返す。
    return input.getKey( AmanatsuKey.PAD_A );
  }

  @Override
  public void UserInit()
  {
    // res内の画像からテクスチャを生成。
    draw.createTexture( R.drawable.title );
    // 画像が正方形でない上にres内にあるため、変な拡大をされる可能性がある。
    // そのため、ここで元の画像サイズを取得しておく。
    w = draw.getTexture( R.drawable.title ).bwidth;
    h = draw.getTexture( R.drawable.title ).bheight;
    // 描画する大きさを予め計算しておく。
    // 高さは端末を横にした時の高さの0.8倍。
    dh = draw.getHeight() * 0.8f;
    // 横幅は縦の2倍なので、描画する高さを倍にする。
    dw = dh * 2.0f;
  }

  @Override
  public boolean MainLoop()
  {
    draw.clearScreen();

    // FPSの表示。
    draw.printf( 0, 0, 0, "FPS:" + draw.getFps() );

    if( timer < 0 )
    {
      // フェードアウト。

      // ロゴのアルファ値を少しずつ下げていく。
      draw.setAlpha( R.drawable.title, Math.abs( (float)timer ) / 30.0f );

      draw.drawTextureScalingC( R.drawable.title, 0.0f, 0.0f, w, h, draw.getWidth() / 2.0f, draw.getHeight() / 2.0f, dw, dh );

      if ( timer == -1 )
      {
        // ゲーム本編へ。
        system.setGameView( new Game() );
      }
    } else if ( timer <= 30)
    {
      // フェードイン。

      // ロゴのアルファ値を少しずつ上げていく。
      draw.setAlpha( R.drawable.title, (float)timer / 30.0f );
      // ゆっくりとロゴが上に動く。
      draw.drawTextureScalingC( R.drawable.title, 0.0f, 0.0f, w, h / 2.0f,
          draw.getWidth() / 2.0f, draw.getHeight() / 2.0f - timer * dh / 4.0f / 30.0f,
          dw, dh / 2.0f );
    } else
    {
      // 待機画面。

      if ( (timer / 20) % 2 == 1 )
      {
        // テクスチャの下半分は隠す。
        draw.drawTextureScalingC( R.drawable.title, 0.0f, 0.0f, w, h / 2.0f, draw.getWidth() / 2.0f, draw.getHeight() / 2.0f - dh / 4.0f, dw, dh / 2.0f );
      } else
      {
        // テクスチャの下半分も表示する。
        draw.drawTextureScalingC( R.drawable.title, 0.0f, 0.0f, w, h, draw.getWidth() / 2.0f, draw.getHeight() / 2.0f, dw, dh );
      }

      if ( getShotButton() > 0 )
      {
        timer = -30;
      }
    }

    ++timer;
    return true;
  }

  @Override
  public void CleanUp()
  {
    draw.destroyTexture( R.drawable.title );
  }
}
