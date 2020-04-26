package graphics;

interface IAnimable {
    public void addAnim(Animation anim);

    public void updateAnim(float interval);

    public void play(int animIndex);
    
    public void stop();
}