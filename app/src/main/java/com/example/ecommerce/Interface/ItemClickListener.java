package com.example.ecommerce.Interface;

import android.view.View;
//merupakan implementasi POLYMORPHISM
//konsep dimana terdapat banyak class yang memiliki signature method yang sama.
// Implementasi dari method-method tersebut diserahkan kepada tiap class,
// akan tetapi cara pemanggilan method harus sama. Agar kita dapat ‘memaksakan’
// signature method yang sama pada banyak class, class tersebut harus diturunkan
// dari sebuah abstract class atau object interface.
public interface ItemClickListener {
    void onClick(View view, int position, boolean isLongCLick);
}
