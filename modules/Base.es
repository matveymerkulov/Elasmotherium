import Math;

/*class Float;
class Question;
Question yes;
Question no;

class Array<ElementType> {
	Int size();
	ElementType at(Int index);
	at(Int index, ElementType value);
}

class Map<KeyType, ValueType> {
	Int size();
	ValueType at(KeyType key);
	at(KeyType key, ValueType value);
}

class Char;
class String {
	Int size();
	Char at(Int index);
}*/

Int screenWidth();
Int screenHeight();

class List<ElementType> {
	Int size();
	add(ElementType element);
}

println(String message);
Int askInt(String message);
say(String message);
Int randomInt(Int value);

exit() {}