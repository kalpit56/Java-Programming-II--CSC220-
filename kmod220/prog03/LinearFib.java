package prog03;

public class LinearFib implements Fib {

	@Override
	public double fib(int n) {
		int a = 0;
		int b = 1;
		int temp = 0;
		// TODO Auto-generated method stub
		for(int i = 0; i < n; i++)
		{
			temp = b;
			b = a + b;
			a = temp;
		}
		return a;
	}

	@Override
	public double O(int n) {
		// TODO Auto-generated method stub
		return n;
	}

}
