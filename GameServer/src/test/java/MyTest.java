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

	private String testClassName(B b){
		return "B";
	}

	private String testClassName(A a){
		return "A";
	}

	@Test
	public void classTest(){
		A obj = new B();
		A objC =new C();
		Assert.assertEquals("B",obj.getName());
		Assert.assertEquals("B",obj.getName());
		Assert.assertEquals("C",objC.getName());
	}

	@Test
	public void test2(){
		A objA = new A();
		A objB = new B();
		Assert.assertEquals("A",testClassName(objA));
		Assert.assertEquals("B",testClassName(objB));
	}
}
