#include <iostream>

typedef enum {
	SUNDAY,
	MONDAY,
	TUESDAY,
	WEDNESDAY,
	THURSDAY,
	FRIDAY,
	SATURDAY
} dow_t;

const char *dows[] = {
	"SUNDAY   ",
	"MONDAY   ",
	"TUESDAY  ",
	"WEDNESDAY",
	"THURSDAY ",
	"FRIDAY   ",
	"SATURDAY "
};

int main ()
{
	int d = SUNDAY;
	int next = MONDAY;
	std::cout << "Day-of-Week(DOW):\tCode" << std::endl;
	std::cout << "----------------------------" << std::endl;
	for (int i = 0; i != 7; ++i) {
		std::cout << dows[i] << "\t" << ":" << "\t" << i << std::endl;
	}

	std::cout << "please enter the day code:";
	std::cin >> d;
	if (d >= SUNDAY && d <= SATURDAY) {

		if (d == SATURDAY) {
			next = SUNDAY;
		} else {
			next = (d + 1);
		}

		switch (next) {
			case SUNDAY:
				std::cout << "tomorrow is SUNDAY" << std::endl;
				break;
			case MONDAY:
				std::cout << "tomorrow is MONDAY" << std::endl;
				break;
			case TUESDAY:
				std::cout << "tomorrow is TUESDAY" << std::endl;
				break;
			case WEDNESDAY:
				std::cout << "tomorrow is WEDNESDAY" << std::endl;
				break;
			case THURSDAY:
				std::cout << "tomorrow is THURSDAY" << std::endl;
				break;
			case FRIDAY:
				std::cout << "tomorrow is FRIDAY" << std::endl;
				break;
			case SATURDAY:
				std::cout << "tomorrow is SATURDAY" << std::endl;
				break;
			default:
				std::cout << "INVALID DAY CODE" << std::endl;
				exit(EXIT_FAILURE);
		}

	} else {
		std::cout << "the input is invalid" << std::endl;
		std::cout << "the input must be in the range 0-6 inclusive" << std::endl;
	}

	return 0;
}
