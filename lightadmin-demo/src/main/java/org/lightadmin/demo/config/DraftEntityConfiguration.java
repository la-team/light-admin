package org.lightadmin.demo.config;

//@Configuration
//@Import( ApplicationConfiguration.class )
//@Administration( type = Entity.class )
public class DraftEntityConfiguration {

//	@Autowired
//	private EntityService entityService;
//
//	@radmin.annotation.ScreenContext
//	protected void configureScreen( ScreenContext screenContext ) {
//		screenContext.screenName( "Entity administration" ).menuName( "My Entity" );
//		screenContext.displayInMenu( displayEntityMenuIfAdmin() );
//	}
//
//	@radmin.annotation.Filter
//	protected Filter filters( final FilterBuilder filterBuilder ) {
//		filterBuilder
//			.field( "name" ).renderer( select( new String[] { "Yes", "No" } ))
//			.field( "price" );
//
//		return filterBuilder.build();
//	}
//
//	@radmin.annotation.Scope
//	protected List<Scopes.Scope> scopes( ScopeBuilder scopeBuilder ) {
//		return scopeBuilder
//			.scope( "All", all() ).defaultScope()
//			.scope( "Published", filter( fieldEq( "published", true ) ))
//			.scope( "Only Profitable", filter( profitablePriceFilter() ) ).build();
//	}
//
//	@radmin.annotation.Validator
//	protected Validator validator() {
//		return null;
//	}
//
//	@radmin.annotation.ListScreen( value = "List screen", fragmentType = ListScreen.FragmentType.BLOG )
//	Fragment listScreen( FragmentBuilder fragmentBuilder ) {
//		return fragmentBuilder
//			.field("name").alias( "title")
//			.field("price").alias("content")
//			.attribute( "date" ).renderer(formula( profitablePriceFormula() )).build();
//	}
//
//	@radmin.annotation.ShowScreen("Show screen")
//	Fragment showScreen( FragmentBuilder fragmentBuilder ) {
//		fragmentBuilder
//			.field( "id" )
//			.field( "name" )
//			.attribute( "profitable price" ).renderer( formula( profitablePriceFormula() ) );
//
//		return fragmentBuilder.build();
//	}
//
//	@radmin.annotation.FormScreen("Form screen")
//	Fragment formScreen() {
//		FormBuilder formBuilder = new SimpleFormBuilder();
//		formBuilder
//			.field( "name" ).renderer( select( new String[] {"Yes", "No"} ) )
//			.field( "price" )
//			.field( "id" );
//		return formBuilder.fragment();
//	}
//
//	@radmin.annotation.Sidebar("Common Sidebar")
//	Fragment commonSidebar() {
//		return Fragments.tiles( "common_sidebar", new ModelMap( ) );
//	}
//
//	@radmin.annotation.Sidebar( value = "Custom sidebar", location = SHOW_SCREEN )
//	Fragment showScreenSidebar() {
//		return Fragments.resource( "resource" );
//	}
//
//	private Function<Entity, String> profitablePriceFormula() {
//		return new Function<Entity, String>() {
//			@Override
//			public String apply( final Entity entity ) {
//				return entityService.calculateProfitablePrice( entity ).toString();
//			}
//		};
//	}
//
//	private Predicate profitablePriceFilter() {
//		return new Predicate<Entity>() {
//			@Override
//			public boolean apply( final Entity entity ) {
//				return entity.getPrice().doubleValue() > 100;
//			}
//		};
//	}
//
//	private Callable<Boolean> displayEntityMenuIfAdmin() {
//		return new Callable<Boolean>() {
//			@Override
//			public Boolean call() throws Exception {
//				return true;
//			}
//		};
//	}
}