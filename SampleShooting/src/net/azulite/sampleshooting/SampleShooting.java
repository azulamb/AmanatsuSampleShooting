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
    setContentView( Amanatsu.autoRunAmanatsu( this, new OP() ) );
  }
}
