package net.azulite.sampleshooting;

public class Object
{
  static final float dis = 10.0f * 10.0f;
  int rx, ry, w, h;
  int hp;
  float x, y, scale;

  public Object( int rx, int ry, int w, int h, float scale )
  {
    this.rx = rx;
    this.ry = ry;
    this.w = w;
    this.h = h;
    this.scale = scale;
  }

  public static float distance( float ax, float ay, float bx, float by )
  {
    ax -= bx;
    ay -= by;
    return ax * ax + by + by;
  }

  public float distance( float x, float y ){ return Object.distance( this.x, this.y, x, y ); }

  public float distance( Object e ){ return Object.distance( x, y, e.x, e.y ); }

  public boolean collisionDetection( Object enemy )
  {
    if ( distance( enemy ) < dis ){ return true; }
    return false;
  }

  public boolean isLive(){ return hp > 0; }
  public boolean isExplosion(){ return hp < 0; }
  public boolean isDead(){ return hp <= 0; }

  public boolean damage( int damage )
  {
    if ( hp > damage )
    {
      hp -= damage;
      return true;
    }
    hp = -60;
    return false;
  }

  public void set( int hp, float x, float y )
  {
    this.hp = hp;
    this.x = x;
    this.y = y;
  }

  public void move(){}

  public boolean draw()
  {
    if ( isLive() )
    {
      Game.draw.drawTextureScalingC( 0, rx, ry, w, h, x, y, scale );
      return true;
    } else if ( isExplosion() )
    {
      ++hp;
    }
    return false;
  }
}
