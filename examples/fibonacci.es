String values = "1, 1";
Int a = 1, b = 1;
for(Int i = 0 ..< 10) {
  Int sum = a + b;
  b = a;
  a = sum;
  values = values + ", " + sum;
}
println(values);