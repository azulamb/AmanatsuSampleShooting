package net.azulite.sampleshooting;

import android.os.Bundle;
import android.app.Activity;
import net.azulite.Amanatsu.*;

public class SampleShooting extends Activity
{

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );

    // Amanatsuのインスタンスを作成しつつ、FPS処理の開始までやってくれる。面倒くさい人向け。
    setContentView( Amanatsu.autoRunAmanatsu( this, new OP() ) );

    /*
    // もしくはこういう記述。
    super.onCreate( savedInstanceState );

    // Amanatsuのインスタンス作成(初めのロゴを消したいならこの記法にするしかない)。
    Amanatsu ama = new Amanatsu( this, new OP() );
    // Viewの登録。
    setContentView( ama.getGLSurfaceView() );
    // FPS処理の開始。
    ama.start();
    */
  }
}
