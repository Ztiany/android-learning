package com.ztiany.gradlemultidex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.ztiany.B;
import com.ztiany.C;
import com.ztiany.D;
import com.ztiany.E;
import com.ztiany.F;
import com.ztiany.G;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        B b = new B();
        C c = new C();
        D d = new D();
        E e = new E();
        F f = new F();
        G g = new G();

        b.a0();
        c.a0();
        d.a0();
        e.a0();
        f.a0();
        g.a0();
        g.z499();

    }

}
