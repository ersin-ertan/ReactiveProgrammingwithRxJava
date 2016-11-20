package com.nullcognition.reactiveprogrammingwithrxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.fernandocejas.frodo.annotation.RxLogObservable;
import rx.Observable;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

  public static int longCalculation(int i) {
    try {
      Thread.sleep(1000 * i);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return i;
  }

  @RxLogObservable public static Observable<Integer> src() {
    return Observable.range(1, 10);
  }

  public static Observable<Observable> obss() {
    return Observable.just(Observable.just(1, 2, 3),
        Observable.from(new Observable[] { Observable.just(1, 2, 3) }),
        Observable.range(0, 10, Schedulers.io()), Observable.never(),
        Observable.error(new Throwable()));
  }

  public static void p(Object o) {
    Log.v("frodo ", o.toString());
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //new P03();


  }
}
