package scott.nursery;

public class Pair<T1, T2>
{
      T1 left;
      T2 right;
      
      
      public Pair(T1 l, T2 r)
      {
          left = l;
          right = r;
      }
      
      public T1 getLeft()
      {
          return left;
      }
      public T2 getRight()
      {
          return right;
      }
}
