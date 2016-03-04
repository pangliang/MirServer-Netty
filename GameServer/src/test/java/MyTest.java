import org.junit.Assert;
import org.junit.Test;

public class MyTest {
	class A {
		protected String name ="A";
		public String getName(){
			return this.name;
		}
	}

	class B extends A{
		public B(){
			name = "B";
		}
	}

	class C extends A{
		private String name = "C";
	}
	@Test
	public void classTest(){
		A obj = new B();
		A objC =new C();
		Assert.assertEquals("B",obj.getName());
		Assert.assertEquals("B",obj.getName());
		Assert.assertEquals("C",objC.getName());
	}
}
